import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent{

  isSpinning: boolean=false;
  signUpForm!:FormGroup;
  signupSuccess = false; 

    constructor(private fb:FormBuilder,
      private authSrvice:AuthService,
      private route:Router){}

    ngOnInit(){
        this.signUpForm=this.fb.group({
          name: [null,[Validators.required]],
          email:[null,[Validators.required,Validators.email]],
          password:[null,Validators.required],
          checkPassword:[null,[Validators.required,this.confirmationValidate]]
        })
    }

    confirmationValidate =(control: FormControl): { [s:string]: boolean}=>{
      if(!control.value){
        return {required: true};
      }else if(control.value!==this.signUpForm.controls['password'].value){
        return {confirm: true,error:true};
      }

      return {}
    };

    register(){
      console.log(this.signUpForm.value);

      this.authSrvice.register(this.signUpForm.value).subscribe((res)=>{
        console.log(res);
        if(res.id!=null){
          this.signupSuccess=true;
          this.route.navigateByUrl("/login")
        }
        else{
          this.signupSuccess=false;
        }
      })
    }
    resetForm() {
      this.signupSuccess = false;
      this.signUpForm.reset();
    }
    
}
