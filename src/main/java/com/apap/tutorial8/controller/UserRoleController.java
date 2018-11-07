package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user, Model model) {
		if(!isValid(user.getPassword())) {
			model.addAttribute("msg","Password harus mengandung huruf, angka, dan minimum 8 karakter");
			return "home";
		}
		model.addAttribute("msg","User baru berhasil ditambahkan");
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping(value = "/ubahPassword")
	private String changePassword(Model model) {
		model.addAttribute("msg","");
		return "changePassword";
	}
	
	@RequestMapping(value = "/ubahPassword", method = RequestMethod.POST)
	private String changePasswordSubmit(@RequestParam(value = "username") String username,
										@RequestParam(value = "password") String password,
										@RequestParam(value = "oldpassword") String oldpassword,
										@RequestParam(value = "passwordConfirm") String passwordConfirm,
										Model model) {
		if(!password.equals(passwordConfirm)) {
			model.addAttribute("msg2","Password baru tidak sesuai");
			return "changePassword";
		}
		UserRoleModel user = userService.findByUsername(username);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (passwordEncoder.matches(oldpassword, user.getPassword())){
			if(!isValid(password)){
				model.addAttribute("msg2","Password harus termasuk huruf, angka, dan minimum 8 karakter");
				return "changePassword";
			}
			userService.updatePassword(user, password);
			model.addAttribute("msg","Password berhasil diubah");
		}
		else {
			model.addAttribute("msg2","Password yang anda masukkan salah");
		}
		return "changePassword";
	}
	
	public boolean isValid(String password) {
		if(!password.matches(".*\\d+.*") || !password.matches(".*[a-zA-Z]+.*") || password.length()<8) return false;
		return true;
	}
}