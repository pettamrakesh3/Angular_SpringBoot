import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth/auth.service';
import { StorageService } from '../services/storage/storage.service';
import {  Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

isSpinning: boolean= true;
loginForm!: FormGroup;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
   ){}

  ngOnInit(): void {
      this.loginForm=this.fb.group({
        email: [null,[Validators.email,Validators.required]],
        password: [null,[Validators.required]]
      })
  }

  login(){
    console.log(this.loginForm.value);
    this.authService.login(this.loginForm.value).subscribe((res)=>{
      console.log(res);
      if(res.userId != null){
        const user ={
            id: res.userId,
            role: res.userRole
        }
        StorageService.saveUser(user);
        StorageService.saveToken(res.jwt);

        StorageService.isAdminLoggedIN();

        if(StorageService.isAdminLoggedIN()){
          this.router.navigateByUrl("/admin/dashboard");
        }
        else if(StorageService.isCustomerLoggedIN()){
          this.router.navigateByUrl("/customer/dashboard");
        }
        
      }
    })
  }
}
