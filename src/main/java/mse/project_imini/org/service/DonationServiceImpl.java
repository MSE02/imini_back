package mse.project_imini.org.service;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import mse.project_imini.org.bean.Donation;
import mse.project_imini.org.dao.DonationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationDao donationDao;
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    public Donation findById(Long id) {
        return donationDao.findById(id).orElse(null);
    }

    @Override
    public List<Donation> findAll() {
        return donationDao.findAll();
    }

    @Override
    public Donation findByEmail(String Email) {
        return donationDao.findByEmail(Email);
    }

    @Override
    @Transactional
    public Donation save(Donation donation) {
        return donationDao.save(donation);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        donationDao.deleteById(id);
    }

    public void createCharge(Donation donation, String token) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        long amountInCents = donation.getAmount().multiply(new BigDecimal(100)).longValue();

        // Create a charge with Stripe
        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("mad")
                        .setDescription("Donation")
                        .setSource(token)
                        .build();

        Charge charge = Charge.create(params);
    }
}