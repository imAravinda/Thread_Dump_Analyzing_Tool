import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { DumpResultComponent } from './components/dump-result/dump-result.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import {MatCardModule} from '@angular/material/card';
import { AnayzeResultCacheService } from './services/analyze_result_service/anayze-result-cache.service';
import {MatTableModule} from '@angular/material/table';
import { DetailsViewerComponent } from './components/details-viewer/details-viewer.component';
import { DiagramModule, SymbolPaletteModule, OverviewModule } from '@syncfusion/ej2-angular-diagrams';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { ScrollToTopComponent } from './components/scroll-to-top/scroll-to-top.component';
import { ToastrModule } from 'ngx-toastr';

const appRoute : Routes = [
  {path:'', component:HomeComponent},
  {path:'analyzing-result',component:DumpResultComponent},
  {path:'threads-details',component:DetailsViewerComponent}
]
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    DumpResultComponent,
    DetailsViewerComponent,
    ScrollToTopComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    RouterModule.forRoot(appRoute),
    HttpClientModule,
    MatCardModule,
    MatTableModule,
    DiagramModule, 
    SymbolPaletteModule, 
    OverviewModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatIconModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot()
  ],
  providers: [AnayzeResultCacheService],
  bootstrap: [AppComponent]
})
export class AppModule { }
