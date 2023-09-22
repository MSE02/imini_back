package mse.project_imini.org.ws;

import mse.project_imini.org.bean.Donation;
import mse.project_imini.org.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @GetMapping("/{id}")
    public Donation findById(@PathVariable Long id) {
        return donationService.findById(id);
    }

    @GetMapping("/")
    public List<Donation> findAll() {
        return donationService.findAll();
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
}