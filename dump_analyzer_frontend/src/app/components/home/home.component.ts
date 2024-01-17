import { Component, OnInit } from '@angular/core';
import { faUpload } from '@fortawesome/free-solid-svg-icons';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AnayzeResultCacheService } from '../../services/analyze_result_service/anayze-result-cache.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ScrollService } from 'src/app/services/scroll_service/scroll.service';
import { ToastrService } from 'ngx-toastr';

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
    private scrollToTop : ScrollService,
    private toaster:ToastrService
  ) {}

  ngOnInit(): void {
    this.scrollToTop.scrollToTop();
  }
  api  = environment.apiUrl;
  selectedFile: File | null = null;
  icon = faUpload;
  lableText : any = "Select a file";
  errorMessage: string | null = null;
  
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
          this.toaster.success("Analyzed Dump File")
          this.router.navigate(['/analyzing-result']);
        },
        (error: HttpErrorResponse) => {
          if (error.status === 500) {
            // Bad Request, handle the specific error from the server
            this.toaster.error(error.error.message)
          } else {
            // Handle other errors, display a generic message
            this.toaster.error('An error occurred while processing the file. Please try again.');
          }
        }
      )
      this.resultService.setDeadlockAnalysisResult(null);
    }
  }
}
