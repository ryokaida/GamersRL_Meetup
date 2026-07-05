package psu.edu.stripe_backend;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.EphemeralKey;
import com.stripe.model.identity.VerificationSession;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.identity.VerificationSessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StripeController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping("/create-verification-session")
    public Map<String, String> createVerificationSession() throws StripeException {
        StripeClient stripeClient = new StripeClient(stripeSecretKey);

        VerificationSessionCreateParams params =
                VerificationSessionCreateParams.builder()
                        .setType(VerificationSessionCreateParams.Type.DOCUMENT)
                        .setProvidedDetails(
                                VerificationSessionCreateParams.ProvidedDetails.builder()
                                        .setEmail("user@example.com")
                                        .build()
                        )
                        .putMetadata("user_id", "class-project-user")
                        .build();

        VerificationSession verificationSession =
                stripeClient.v1().identity().verificationSessions().create(params);

        EphemeralKeyCreateParams ephemeralKeyParams =
                EphemeralKeyCreateParams.builder()
                        .putExtraParam("verification_session", verificationSession.getId())
                        .setStripeVersion("2026-05-27.dahlia")
                        .build();

        EphemeralKey ephemeralKey =
                stripeClient.v1().ephemeralKeys().create(ephemeralKeyParams);

        return Map.of(
                "id", verificationSession.getId(),
                "ephemeral_key_secret", ephemeralKey.getSecret()
        );
    }
}