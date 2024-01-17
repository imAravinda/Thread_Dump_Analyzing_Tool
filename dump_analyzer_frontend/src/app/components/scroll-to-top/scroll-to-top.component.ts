import { Component, HostListener, OnInit } from '@angular/core';
import { faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { ScrollService } from 'src/app/services/scroll_service/scroll.service';

@Component({
  selector: 'app-scroll-to-top',
  templateUrl: './scroll-to-top.component.html',
  styleUrls: ['./scroll-to-top.component.css']
})
export class ScrollToTopComponent implements OnInit {

  upArrow = faArrowUp;

  constructor(private scrollToTop :ScrollService) { }

  ngOnInit(): void {
  }
  
  showScrollToTop = this.scrollToTop.scrollToTop;

  scrollToTopFn() {
    this.scrollToTop.scrollToTop();
  }

}
