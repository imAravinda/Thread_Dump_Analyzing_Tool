import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShareDataService {
  private shareData : BehaviorSubject<any>  = new BehaviorSubject<any>(null);

  setShareData(shareData:any):void{
    this.shareData.next(shareData);
  }

  getShareData():Observable<any>{
    return this.shareData.asObservable();
  }
}
