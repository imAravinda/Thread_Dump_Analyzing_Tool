import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  
  constructor(private router: Router, private route: ActivatedRoute) {
    
   }

  ngOnInit(): void {
    
  }
  showAdditionalText: boolean = false;

  setCurrentPage():void{
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      console.log(this.route.snapshot.firstChild?.routeConfig?.path);
      
      this.showAdditionalText = this.route.snapshot.firstChild?.routeConfig?.path !== '';
    });
  }

}
