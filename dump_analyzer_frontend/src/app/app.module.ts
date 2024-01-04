import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { NavbarComponent } from './navbar/navbar.component';
import { DumpResultComponent } from './dump-result/dump-result.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import {MatCardModule} from '@angular/material/card';
import { AnayzeResultCacheService } from './anayze-result-cache.service';
import {MatTableModule} from '@angular/material/table';
import { DetailsViewerComponent } from './details-viewer/details-viewer.component';
import { DiagramModule, SymbolPaletteModule, OverviewModule } from '@syncfusion/ej2-angular-diagrams';
import {MatExpansionModule} from '@angular/material/expansion';

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
    DetailsViewerComponent
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
    MatExpansionModule
  ],
  providers: [AnayzeResultCacheService],
  bootstrap: [AppComponent]
})
export class AppModule { }
