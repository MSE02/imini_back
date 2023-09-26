package mse.project_imini.org.ws;

import mse.project_imini.org.bean.Donation;
import mse.project_imini.org.service.Impl.DonationServiceImpl;
import mse.project_imini.org.service.facade.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;
    @Autowired
    private DonationServiceImpl donationServiceImpl;

    @GetMapping("/{id}")
    public Donation findById(@PathVariable Long id) {
        return donationService.findById(id);
    }

    @GetMapping("/")
    public List<Donation> findAll() {
        return donationServiceImpl.findAll();
    }

    @GetMapping("/code")
    public String randomCodeGenerator() {
        return donationServiceImpl.randomCodeGenerator();
    }

    @GetMapping("/email/{donorEmail}")
    public Donation findByEmail(@PathVariable String donorEmail) {
        return donationService.findByEmail(donorEmail);
    }

    @PostMapping("/")
    public Donation save(@RequestBody Donation donation) {
        return donationService.save(donation);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        donationService.deleteById(id);
    }

    @PostMapping("/SendCode")
    public ResponseEntity<String> donateAndSendVerificationEmail(@RequestBody Donation donation) {
        // Check if the donation object is valid, e.g., not null and has required fields
        if (donation == null || StringUtils.isEmpty(donation.getEmail()) || StringUtils.isEmpty(donation.getF_name()) || StringUtils.isEmpty(donation.getL_name())) {
            return ResponseEntity.badRequest().body("Invalid donation data");
        }

        // Check if a donation with the same email already exists
        Donation existingDonation = donationService.findByEmail(donation.getEmail());
        if (existingDonation != null) {
            return ResponseEntity.badRequest().body("A donation with this email already exists");
        }

        // Save the donation to the database
        Donation savedDonation = donationService.save(donation);

        if (savedDonation != null) {
            // Generate a verification code and save it to the donation
            Donation donationWithCode = donationServiceImpl.generateVerificationCode(savedDonation.getEmail());

            if (donationWithCode != null) {
                // Send the verification email
                donationServiceImpl.sendEmail(donationWithCode);

                return ResponseEntity.ok("Donation created successfully. Verification email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate verification code.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save donation.");
        }
    }

}