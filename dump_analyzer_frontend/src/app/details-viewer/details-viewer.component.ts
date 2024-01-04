import { Component, OnInit } from '@angular/core';
import { AnayzeResultCacheService } from '../anayze-result-cache.service';


@Component({
  selector: 'app-details-viewer',
  templateUrl: './details-viewer.component.html',
  styleUrls: ['./details-viewer.component.css']
})
export class DetailsViewerComponent implements OnInit {

  constructor(private analyzedresult: AnayzeResultCacheService) { }

  ngOnInit(): void {
    this.getThreadsDetailsByFilter();
  }
  dataSource : any;
  displayedDetails: any;
  showAll: boolean = false;
  thread:any;

  getThreadsDetailsByFilter():void{
    this.analyzedresult.getAnalysisFilteredResult().subscribe(
      result=>{
        this.dataSource = result.threadDumps;
        this.thread = result.threadDumps[0].state;
        this.displayedDetails = this.dataSource.slice(0,3);
      }
    )
  }

  toggleShowAllRows() {
    this.showAll = !this.showAll;
    this.displayedDetails = this.showAll ? this.dataSource : this.dataSource.slice(0,3);
  }

}
