package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.shared.data.ProductEntity
import com.pisco.stockmanager.shared.domain.CartItem
import com.pisco.stockmanager.shared.domain.CheckoutRepository
import com.pisco.stockmanager.shared.domain.CheckoutResult
import com.pisco.stockmanager.shared.domain.ProductRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaisseScreen() {

    val productRepository = koinInject<ProductRepository>()
    val checkoutRepository = koinInject<CheckoutRepository>()
    val scope = rememberCoroutineScope()

    val products by productRepository.getProducts()
        .collectAsState(initial = emptyList())

    var search by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.name.contains(search, ignoreCase = true)
    }

    var cart by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    val total = cart.sumOf { it.product.price * it.quantity }

    var showQuantityDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var quantityInput by remember { mutableStateOf("") }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    fun addToCart(product: ProductEntity, quantity: Int) {
        val existing = cart.find { it.product.id == product.id }
        cart = if (existing != null) {
            cart.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it
            }
        } else {
            cart + CartItem(product = product, quantity = quantity)
        }
    }

    fun increaseQuantity(productId: Int) {
        cart = cart.map { item ->
            if (item.product.id == productId) {
                if (item.quantity < item.product.quantity) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    snackbarMessage = "Stock maximum atteint"
                    item
                }
            } else item
        }
    }

    fun decreaseQuantity(productId: Int) {
        cart = cart.mapNotNull { item ->
            if (item.product.id == productId) {
                val newQty = item.quantity - 1
                if (newQty <= 0) null else item.copy(quantity = newQty)
            } else item
        }
    }

    fun removeFromCart(productId: Int) {
        cart = cart.filter { it.product.id != productId }
    }

    Scaffold(

        containerColor = GreenPrimary,

        topBar = {
            TopAppBar(
                title = { Text("Caisse", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary,
                    titleContentColor = Color.White
                )
            )
        },

        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(16.dp)
        ) {

            Row(modifier = Modifier.weight(1f)) {

                // ================= Panneau Produits (gauche) =================
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Rechercher un produit") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn {

                            items(filteredProducts) { product ->

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column {
                                        Text(product.name)
                                        Text("${formatCfa(product.price)} CFA")
                                        Text(
                                            text = "Stock : ${product.quantity}",
                                            color = if (product.quantity <= 0)
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Button(
                                        modifier = Modifier.height(40.dp),
                                        enabled = product.quantity > 0,
                                        onClick = {
                                            selectedProduct = product
                                            quantityInput = ""
                                            showQuantityDialog = true
                                        }
                                    ) {
                                        Text("Ajouter")
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // ================= Panier + Paiement (droite) =================
                Column(modifier = Modifier.weight(1f)) {

                    Card(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("Panier", style = MaterialTheme.typography.titleMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            LazyColumn(modifier = Modifier.weight(1f)) {

                                items(cart) { item ->

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Text(item.product.name)

                                        Row {

                                            Button(onClick = { decreaseQuantity(item.product.id) }) {
                                                Text("-")
                                            }

                                            Text(
                                                item.quantity.toString(),
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )

                                            Button(onClick = { increaseQuantity(item.product.id) }) {
                                                Text("+")
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Button(onClick = { removeFromCart(item.product.id) }) {
                                                Text("❌")
                                            }
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                onClick = {

                                    if (cart.isEmpty()) {
                                        snackbarMessage = "Le panier est vide"
                                        return@Button
                                    }

                                    val cartSnapshot = cart

                                    scope.launch {
                                        when (val result = checkoutRepository.validateSale(
                                            cart = cartSnapshot,
                                            clientId = 1
                                        )) {

                                            is CheckoutResult.Success -> {
                                                cart = emptyList()
                                                snackbarMessage = "Vente enregistrée avec succès"
                                            }

                                            is CheckoutResult.InsufficientStock -> {
                                                snackbarMessage =
                                                    "Stock insuffisant : ${result.productNames.joinToString(", ")}"
                                            }

                                            is CheckoutResult.EmptyCart -> {
                                                snackbarMessage = "Le panier est vide"
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text("Total : ${formatCfa(total)} CFA\nValider Facture")
                            }
                        }
                    }
                }
            }
        }
    }

    // ================= Dialogue Quantité =================
    if (showQuantityDialog) {

        AlertDialog(
            onDismissRequest = { showQuantityDialog = false },
            title = { Text("Quantité") },
            text = {
                OutlinedTextField(
                    value = quantityInput,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() }) quantityInput = it
                    },
                    label = { Text("Quantité") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val qty = quantityInput.toIntOrNull()
                        val product = selectedProduct

                        if (qty != null && product != null && qty in 1..product.quantity) {
                            addToCart(product, qty)
                            showQuantityDialog = false
                        } else {
                            snackbarMessage = "Stock insuffisant ou quantité invalide"
                        }
                    }
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                Button(onClick = { showQuantityDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}