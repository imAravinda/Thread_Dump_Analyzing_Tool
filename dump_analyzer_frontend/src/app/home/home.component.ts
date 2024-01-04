import { Component, OnInit } from '@angular/core';
import { faUpload } from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';
import { AnayzeResultCacheService } from '../anayze-result-cache.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  constructor(
    private http: HttpClient,
    private resultService: AnayzeResultCacheService,
    private router: Router,
  ) {}

  ngOnInit(): void {
  }
  api  = environment.apiUrl;
  selectedFile: File | null = null;
  icon = faUpload;
  lableText : any = "Select a file";
  
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    this.lableText = this.selectedFile?.name;
  }

  uploadAndAnalyze(): void {
    if (this.selectedFile) {
      const formData: FormData = new FormData();
      formData.append('file', this.selectedFile, this.selectedFile.name);      
      this.http.post(`${this.api}analyze`, formData)
      .subscribe(
        (result)=>{
          this.resultService.setAnalysisResult(result);
          this.router.navigate(['/analyzing-result']);
        }
      )
      this.resultService.setDeadlockAnalysisResult(null);
    }
  }
}
