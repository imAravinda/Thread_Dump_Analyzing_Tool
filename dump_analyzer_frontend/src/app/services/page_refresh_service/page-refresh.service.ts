import { Injectable } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class PageRefreshService {
  constructor(private router:Router) {
  }

  init() {
    if (performance.navigation.type === 1) {
      this.router.navigate(['']);
  }
}
}
