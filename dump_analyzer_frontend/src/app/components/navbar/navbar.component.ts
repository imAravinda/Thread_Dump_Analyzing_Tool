import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  
  constructor() {
   }

  ngOnInit(): void {
    
  }

  url = environment.production ? environment.prodUrl : environment.localURL;

  openNewWindow() : void{
    window.open(this.url,'_blank');
  }
}
