package mse.project_imini.org.dao;


import mse.project_imini.org.bean.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface DonationDao extends JpaRepository<Donation, Long> {
    Donation findByEmail(String donorEmail);
}
