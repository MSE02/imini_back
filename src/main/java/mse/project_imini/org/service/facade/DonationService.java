package mse.project_imini.org.service.facade;



import mse.project_imini.org.bean.Donation;

import java.util.List;

public interface DonationService {
    Donation findById(Long id);
    List<Donation> findAll();
    Donation findByEmail(String Email);
    Donation save(Donation donation);
    void deleteById(Long id);
}