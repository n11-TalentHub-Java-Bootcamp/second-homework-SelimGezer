package com.selim.springboot.controller;

import com.selim.springboot.dto.UserDto;
import com.selim.springboot.entity.User;
import com.selim.springboot.service.entityservice.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping("")
    public List<User> findAll(){
        return userEntityService.findAll();
    }

    @GetMapping(value = "/names")
    public User getByUsername(
            @RequestParam(value ="username") String username){
        return userEntityService.getByKullaniciAdi(username);
    }

    @GetMapping(value = "/phones",params = {"phoneNumber"})
    public User getByPhonenumber(@RequestParam(value = "phoneNumber") String phoneNumber){
        return userEntityService.getByTelefon(phoneNumber);
    }

    @PostMapping("")
    public void addUser(@RequestBody UserDto userDto){
        userEntityService.insertUser(userDto);
    }

    @DeleteMapping(value = "/deleteUserById",params = {"kullaniciAdi","telefon"})
    public ResponseEntity<String> deleteById(@RequestParam(value="kullaniciAdi") String kullaniciAdi, @RequestParam(value = "telefon") String kullaniciTel ){

        User kullaniciByUserAdi = userEntityService.getByKullaniciAdi(kullaniciAdi);
        String mesaj=null;
        if(kullaniciByUserAdi ==null){
            System.out.println("Belirtilen kullaniciAdi ile ilgili kullanıcı bulunamadı!");
            mesaj=String.format("%s kullanıcı adına sahip bir kullanıcı bulunamadı!",kullaniciAdi);
            return new ResponseEntity(mesaj,HttpStatus.OK);
        }else{
            if(kullaniciByUserAdi.getTelefon().equals(kullaniciTel)){
                userEntityService.deleteById(kullaniciByUserAdi.getId());
                return new ResponseEntity("Kullanıcı başarıyla silindi.",HttpStatus.OK);
            }else{
                mesaj=String.format("Kullanıcı Adı: %s ile belirtilen %s numaralı telefon numarası eşleşmiyor!",kullaniciAdi,kullaniciTel);
                return new ResponseEntity(mesaj,HttpStatus.OK);
            }
        }
    }

   @PutMapping(value = "",params = {"id"})
    public ResponseEntity updateUser(@RequestParam(value="id") Long id,@RequestBody UserDto userDto){
       return userEntityService.updateKullaniciByID(id, userDto);
    }

}
