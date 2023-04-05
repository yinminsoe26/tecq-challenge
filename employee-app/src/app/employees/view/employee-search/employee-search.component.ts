import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { EmployeeSearchData } from '../../model/employee-search-data.model';

@Component({
  selector: 'app-employee-search',
  templateUrl: './employee-search.component.html',
  styleUrls: ['./employee-search.component.css']
})
export class EmployeeSearchComponent implements OnInit {
  employeeSearchedData: EmployeeSearchData = new EmployeeSearchData('','', 0, 30, '+id');
  @Output("employeeSearched") employeeSearched = new EventEmitter<EmployeeSearchData>();
  constructor(){
  }

  ngOnInit(): void {
    //this.decimalFilter.emit();
  }
  decimalFilter(event: any) {
    const reg = /^-?\d*(\.\d{0,2})?$/;
    let input = event.target.value + String.fromCharCode(event.charCode);
 
    if (!reg.test(input)) {
        event.preventDefault();
    }
 }  

 
 onSearchEmployee(){
  this.employeeSearched.emit(this.employeeSearchedData);
  console.log("Search Employee"+this.employeeSearchedData.minSalary+" max "+this.employeeSearchedData.maxSalary);
  
 }
}
