import { Component, OnInit } from '@angular/core';
import { StorageService } from './auth/components/services/storage/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'CarRental_Angular';

  isCustomerLOggedIn: boolean = StorageService.isCustomerLoggedIN();

  isAdminLoggedIn: boolean = StorageService.isAdminLoggedIN();

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event.constructor.name === "NavigationEnd") {
        this.isAdminLoggedIn = StorageService.isAdminLoggedIN();

        this.isCustomerLOggedIn = StorageService.isCustomerLoggedIN();
      }
    })
  }

  logout(){
    StorageService.logout();
    this.router.navigateByUrl("/login");
  }
}
