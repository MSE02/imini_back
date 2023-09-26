package mse.project_imini.org.service.Impl;

import mse.project_imini.org.bean.Donation;
import mse.project_imini.org.dao.DonationDao;
import mse.project_imini.org.service.facade.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    private final JavaMailSenderImpl mailSender;

    @Autowired
    private DonationDao donationDao;

    public DonationServiceImpl(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Donation findById(Long id) {
        return donationDao.findById(id).orElse(null);
    }

    @Override
    public List<Donation> findAll() {
        return donationDao.findAll();
    }

    @Override
    public Donation findByEmail(String email) {
        return donationDao.findByEmail(email);
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

    public String randomCodeGenerator() {
        String AlphaNumericString = "0123456789";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public Donation generateVerificationCode(String email) {
        String code = randomCodeGenerator();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        Donation donation = donationDao.findByEmail(email);
        donation.setVerificationCode(code);
        donation.setVerificationCodeExpirationTime(expirationTime);

        donationDao.save(donation);

        return donation;
    }

    public boolean verifyEmail(String email, String code) {
        Donation donation = donationDao.findByEmail(email);

        return donation != null
                && donation.getVerificationCode() != null
                && donation.getVerificationCode().equals(code)
                && !donation.getVerificationCodeExpirationTime().isBefore(LocalDateTime.now());
    }

    public void sendEmail(Donation donation) {
        if (findById(donation.getId()) != null) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            String body = "Cher " + donation.getF_name() + " " + donation.getL_name() + "."
                    + "\n\nVotre code de vérification est " + donation.getVerificationCode();
            String subject = "Code de Vérification";
            simpleMailMessage.setFrom("moha2000mse@gmail.com");
            simpleMailMessage.setText(body);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setTo(donation.getEmail());
            this.mailSender.send(simpleMailMessage);
        }
    }
}
