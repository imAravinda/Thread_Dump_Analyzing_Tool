import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnayzeResultCacheService {

  private analysisResultSubject : BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private analysisFilteredResult : BehaviorSubject<any> = new BehaviorSubject<any>(null);
  private analyzeDeadlockDetails : BehaviorSubject<any> = new BehaviorSubject<any>(null);

  setAnalysisResult(result: any): void {
    this.analysisResultSubject.next(result);
  }

  getAnalysisResult(): Observable<any> {
    return this.analysisResultSubject.asObservable();
  }

  setAnalysisFilteredResult(result:any):void{
    this.analysisFilteredResult.next(result);
  }

  getAnalysisFilteredResult(): Observable<any>{
    return this.analysisFilteredResult.asObservable();
  }

  setDeadlockAnalysisResult(result: any): void {
    this.analyzeDeadlockDetails.next(result);
  }

  getDeadlockAnalysisResult(): Observable<any> {
    return this.analyzeDeadlockDetails.asObservable();
  }
}
