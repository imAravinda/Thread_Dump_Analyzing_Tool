import { HostListener, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScrollService {

  showScrollToTop = false;

  constructor() { }

  @HostListener('window:scroll', [])
  onWindowScroll() : boolean {
    return this.showScrollToTop = window.pageYOffset > 200;
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  scrollToSection(sectionId : string){
    const element = document.getElementById(sectionId);
    if(element){
      element.scrollIntoView({behavior : 'smooth'});
    }
  }

}
