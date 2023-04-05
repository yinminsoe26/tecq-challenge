import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ChildrenOutletContexts, RouterModule } from '@angular/router';
import {  MatTableModule } from '@angular/material/table';
import {  MatPaginatorModule } from '@angular/material/paginator';
import {MatSortModule} from '@angular/material/sort';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { AppComponent } from './app.component';


import { SideMenuComponent } from './side-menu/side-menu.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EmployeeSearchComponent } from './employees/view/employee-search/employee-search.component';
import { UploadEmployeesComponent } from './employees/view/upload-employees/upload-employees.component';
import { EmployeesListComponent } from './employees/view/employees-list/employees-list.component';


@NgModule({
  declarations: [
    AppComponent,
    SideMenuComponent,
    EmployeeSearchComponent,
    UploadEmployeesComponent,
    EmployeesListComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    RouterModule.forRoot([
      { path: '', pathMatch: 'full', redirectTo: 'employee-dashboard' },
      {path: 'upload-employees', component: UploadEmployeesComponent},
      {path: 'employee-dashboard', component: EmployeesListComponent}
    ]),
    BrowserAnimationsModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {

  constructor(private contexts: ChildrenOutletContexts) {}

  getRouteAnimationData() {
    return this.contexts.getContext('primary')?.route?.snapshot?.data?.['animation'];
  }


 }
