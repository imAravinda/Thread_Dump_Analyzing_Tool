import { Component, HostListener, OnInit } from '@angular/core';
import { AnayzeResultCacheService } from '../../services/analyze_result_service/anayze-result-cache.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ScrollService } from 'src/app/services/scroll_service/scroll.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ShareDataService } from 'src/app/services/share_data_service/share-data.service';


@Component({
  selector: 'app-details-viewer',
  templateUrl: './details-viewer.component.html',
  styleUrls: ['./details-viewer.component.css']
})
export class DetailsViewerComponent implements OnInit {

  constructor(private analyzedresult: AnayzeResultCacheService,private scroll : ScrollService,private router:Router,private shareData : ShareDataService) { }

  ngOnInit(): void {
    this.getThreadsDetailsByFilter();
    this.scroll.scrollToTop();
  }
  api = environment.apiUrl;
  dataSource : any;
  displayedDetails: any;
  threadDumps : any = [];
  showAll: boolean = false;
  thread:any;
  pkgName : string = '';
  sectionID : any;

  getThreadsDetailsByFilter():void{
    this.analyzedresult.getAnalysisFilteredResult().subscribe(
      result=>{
        this.dataSource = result.threadDumps == undefined ? result : result.threadDumps;
        this.thread = result.threadDumps == undefined ? result[0].state : result.threadDumps[0].state;
        this.displayedDetails = this.dataSource.slice(0,3);
      }
    )
  }

  searchByPackage() : void{
    const filterDumps : any[] = [];
    this.analyzedresult.getAnalysisFilteredResult().subscribe(
      result => {
        this.dataSource = result.threadDumps == undefined ? result : result.threadDumps;        
        this.dataSource.map((threadDump: any)=>{
          if(threadDump.packageDetailsAffectedByThread != null){
            if(threadDump.packageDetailsAffectedByThread.toLowerCase().includes(this.pkgName.toLowerCase())){            
              filterDumps.push(threadDump);
            }
          }
        })
        this.displayedDetails = filterDumps;
      }
    );
  }

  toggleShowAllRows() {
    this.showAll = !this.showAll;
    this.displayedDetails = this.showAll ? this.dataSource : this.dataSource.slice(0,3);
  }

  @HostListener('window:popstate',['$event'])
  onPopState(event: any): void {
    setTimeout(() => {
      this.shareData.getShareData().subscribe(data => {
        this.sectionID = data;
      });
      this.router.navigate(['/analyzing-result'], { fragment: this.sectionID });
    }, 10);
  }
}
