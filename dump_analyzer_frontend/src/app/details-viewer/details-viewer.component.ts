import { Component, OnInit } from '@angular/core';
import { AnayzeResultCacheService } from '../anayze-result-cache.service';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-details-viewer',
  templateUrl: './details-viewer.component.html',
  styleUrls: ['./details-viewer.component.css']
})
export class DetailsViewerComponent implements OnInit {

  constructor(private analyzedresult: AnayzeResultCacheService,private http: HttpClient) { }

  ngOnInit(): void {
    this.getThreadsDetailsByFilter();
  }
  api = environment.apiUrl;
  dataSource : any;
  displayedDetails: any;
  threadDumps : any = [];
  showAll: boolean = false;
  thread:any;
  pkgName : string = '';

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

  searchByPackage() : void{
    const filterDumps : any[] = [];
    this.analyzedresult.getAnalysisFilteredResult().subscribe(
      result => {
        this.dataSource = result;
        console.log(this.dataSource);
        
        this.dataSource.threadDumps.map((threadDump: any)=>{
          if(threadDump.packageDetailsAffectedByThread != null){
            if(threadDump.packageDetailsAffectedByThread.toLowerCase().includes(this.pkgName.toLowerCase())){            
              filterDumps.push(threadDump);
            }
          }
        })
        this.displayedDetails = filterDumps.slice(0,3);
      }
    );
  }

}
