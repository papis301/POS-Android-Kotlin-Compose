package com.pisco.stockmanager.presentation.screen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pisco.stockmanager.ui.theme.BluePrimary

/**
 * Coordonnées affichées au client pour :
 * - le support technique
 * - l'activation / l'achat de modules payants
 *
 * À adapter avec les vraies informations avant publication.
 */
private object SupportContact {

    const val PHONE = "+221 76 648 74 20"

    // Format international sans "+" ni espaces, requis par le lien wa.me
    const val WHATSAPP_NUMBER = "221766487420"

    const val EMAIL = "support@stockmanager.app"

    const val WEBSITE = "en cours "

    const val WEBSITE_LABEL = ""

    // ID ou nom d'utilisateur de la page (visible dans l'URL : facebook.com/<ID>)
    const val FACEBOOK_PAGE_ID = "61591494425042"

    const val FACEBOOK_URL = "https://facebook.com/$FACEBOOK_PAGE_ID"

    const val FACEBOOK_LABEL = "Yombena"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    navController: NavController
) {

    val context = LocalContext.current

    Scaffold(

        containerColor = BluePrimary,

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Contact & Support",
                        color = Color.White
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Besoin d'aide ou d'un module supplémentaire ?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Contactez-nous directement, nous répondons rapidement.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            ContactActionCard(
                icon = Icons.Filled.Chat,
                iconBackground = Color(0xFF25D366),
                title = "WhatsApp",
                subtitle = SupportContact.PHONE,
                onClick = {
                    openWhatsApp(context, SupportContact.WHATSAPP_NUMBER)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactActionCard(
                icon = Icons.Filled.ThumbUp,
                iconBackground = Color(0xFF1877F2),
                title = "Facebook",
                subtitle = SupportContact.FACEBOOK_LABEL,
                onClick = {
                    openFacebook(
                        context = context,
                        pageId = SupportContact.FACEBOOK_PAGE_ID,
                        fallbackUrl = SupportContact.FACEBOOK_URL
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ContactActionCard(
                icon = Icons.Filled.Phone,
                iconBackground = BluePrimary,
                title = "Appeler",
                subtitle = SupportContact.PHONE,
                onClick = {
                    dialPhone(context, SupportContact.PHONE)
                }
            )

//            Spacer(modifier = Modifier.height(12.dp))
//
//            ContactActionCard(
//                icon = Icons.Filled.Email,
//                iconBackground = Color(0xFFEA4335),
//                title = "Email",
//                subtitle = SupportContact.EMAIL,
//                onClick = {
//                    sendEmail(context, SupportContact.EMAIL)
//                }
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            ContactActionCard(
//                icon = Icons.Filled.Language,
//                iconBackground = Color(0xFF6C63FF),
//                title = "Site web",
//                subtitle = SupportContact.WEBSITE_LABEL,
//                onClick = {
//                    openWebsite(context, SupportContact.WEBSITE)
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ContactActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBackground: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = iconBackground,
                            shape = CircleShape
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun openFacebook(
    context: android.content.Context,
    pageId: String,
    fallbackUrl: String
) {

    val intent = try {

        context.packageManager.getPackageInfo("com.facebook.katana", 0)

        // L'app Facebook est installée : on ouvre directement la page dedans
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("fb://page/$pageId")
        )

    } catch (e: PackageManager.NameNotFoundException) {

        // Pas d'app Facebook : on ouvre le lien web classique
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(fallbackUrl)
        )
    }

    launchSafely(context, intent, "Impossible d'ouvrir Facebook")
}

private fun openWhatsApp(
    context: android.content.Context,
    whatsappNumber: String
) {

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://wa.me/$whatsappNumber")
    )

    launchSafely(context, intent, "WhatsApp n'est pas installé")
}

private fun dialPhone(
    context: android.content.Context,
    phone: String
) {

    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$phone")
    )

    launchSafely(context, intent, "Impossible d'ouvrir le composeur")
}

private fun sendEmail(
    context: android.content.Context,
    email: String
) {

    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.parse("mailto:$email")
    )

    launchSafely(context, intent, "Aucune application email trouvée")
}

private fun openWebsite(
    context: android.content.Context,
    url: String
) {

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )

    launchSafely(context, intent, "Impossible d'ouvrir le navigateur")
}

private fun launchSafely(
    context: android.content.Context,
    intent: Intent,
    errorMessage: String
) {

    try {

        context.startActivity(intent)

    } catch (e: ActivityNotFoundException) {

        Toast.makeText(
            context,
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
}