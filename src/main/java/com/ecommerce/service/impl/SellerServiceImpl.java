package com.ecommerce.service.impl;

import com.ecommerce.Enum.AccountStatus;
import com.ecommerce.Enum.USER_ROLE;
import com.ecommerce.config.JwtProvider;
import com.ecommerce.modal.Address;
import com.ecommerce.modal.Seller;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null) {
            throw new Exception("seller already exist, use different email");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGstin(seller.getGstin());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        return sellerRepository.findById(id).orElseThrow(() -> new Exception("seller not found with id " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {

        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("seller not found...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller existingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }

        if (seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails() != null
                && seller.getBusinessDetails().getBusinessName() != null) {

            existingSeller.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName());
        }

        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null) {

            existingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(
                    seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(
                    seller.getBankDetails().getIfscCode());
        }

        if (seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null) {

            existingSeller.getPickupAddress()
                    .setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress()
                    .setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress()
                    .setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress()
                    .setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress()
                    .setPinCode(seller.getPickupAddress().getPinCode());
        }

        if (seller.getGstin() != null) {
            existingSeller.setGstin(seller.getGstin());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {

        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {

        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
