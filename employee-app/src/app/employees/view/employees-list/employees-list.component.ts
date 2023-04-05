import {AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatPaginator, MatPaginatorIntl} from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { EmployeeSearchData } from '../../model/employee-search-data.model';
import { Employee } from '../../model/employee.model';
import { EmployeeService } from '../../../service/employee.service';
declare var window: any;
 
@Component({
  selector: 'app-employees-list',
  templateUrl: './employees-list.component.html',
  styleUrls: ['./employees-list.component.css']
})
export class EmployeesListComponent implements OnInit, AfterViewInit{
  @ViewChild('empTbSort') empTbSort = new MatSort();
  @ViewChild(MatPaginator) paginator: MatPaginator = new MatPaginator(new MatPaginatorIntl(), ChangeDetectorRef.prototype);

  //@Input("employeeList") employees: Employee[] = [];
  // @Output("clickedToDeleteEmployee") clickedToDeleteEmployee = new EventEmitter<Employee>;
  // @Output("employeeSaved") employeeSaved = new EventEmitter<Employee>();
 
  image: string = 'assets/funIcon.png';
  displayedColumns: string[] = ['path', 'id', 'name', 'login', 'salary', 'action'];

 
  //bind employee list
  employees: Employee[]  = [];
  //temporary employee list
  tempFilterEmployeesResult: Employee[] =[];
  //employee Modal
  editEmployee: Employee = new Employee('', '', '', '', '');
  formModal: any;
  //Search Area Bind Data
  employeeSearchedData: EmployeeSearchData =new EmployeeSearchData('0', '0',0,30,'+id');

  //status messages 
  message: string = '';
  errorMessage: string = '';
  //Material table data source
  dataSource:MatTableDataSource<Employee> = new MatTableDataSource<Employee>(this.employees);
 
  //intialized the employee Service
  constructor(private employeeService: EmployeeService) {}
 
  ngOnInit(): void {
    //formModal initialization
    this.formModal = new window.bootstrap.Modal(
      document.getElementById("employeeModal")
    );

    this.employeeService.fetchEmployeesBySalaryRange(this.employeeSearchedData)
    .subscribe(
      (response) => {
        this.tempFilterEmployeesResult=[];
        response.map(item => {
          var employee = item;
          employee.path=this.image;
          this.tempFilterEmployeesResult.push(employee);
        });
        this.employees=this.tempFilterEmployeesResult;
        this.bindDataToTable();
      },
      

    );
  }
  
  ngAfterViewInit() {
    this.bindDataToTable();
  }

  bindDataToTable(){
    this.dataSource = new MatTableDataSource<Employee>(this.employees);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.empTbSort;
  }

  decimalFilter(event: any) {
    const reg = /^-?\d*(\.\d{0,2})?$/;
    let input = event.target.value + String.fromCharCode(event.charCode);
 
    if (!reg.test(input)) {
        event.preventDefault();
    }
  }  
  openModal(){
    console.log("open Modal ");
      this.formModal.show();
  }
  //Edit Employee Modal Form Action
  onEditEmployee(editEmployee: Employee){
    console.log("Employee Id of onEditEmployee in employee List is::"+editEmployee.id);
    this.editEmployee=editEmployee;
    this.openModal();
  }

  onSaveEmployee(){
    this.employeeService.saveEmployee(this.editEmployee)
    .subscribe(
      subscribeEmployee => {
        subscribeEmployee.path=this.image;
        let indexToUpdate = this.employees.findIndex(eachEmployee => eachEmployee.id === subscribeEmployee.id);
        this.employees[indexToUpdate] = subscribeEmployee;
        this.bindDataToTable();
        this.message = '('+this.editEmployee.id+') Updated successfully';
      }
    ); 
    this.formModal.hide();
  }

  onDeleteEmployee(deleteEmployee: Employee){
    console.log("Employee Id of onDeleteEmployee in employee List is::"+deleteEmployee.id);
    //call server to delete
    this.employeeService.deleteEmployee(deleteEmployee.id)
    .subscribe(() => {
      let indexToRemove = this.employees.findIndex(eachEmployee => eachEmployee.id === deleteEmployee.id);
      this.employees.splice(indexToRemove, 1);
      this.bindDataToTable();
      this.message =  '('+deleteEmployee.id+')  Deleted successfully.';
    });
  }

  onEmployeeSearched(employeeSearchedData: EmployeeSearchData) {
    console.log("onEmployeeSearched" + employeeSearchedData.minSalary + ", max :" + employeeSearchedData.maxSalary);
    let minSalary = parseFloat(employeeSearchedData.minSalary);
    let maxSalary = parseFloat(employeeSearchedData.maxSalary);
    let bothIsNan=Number.isNaN(minSalary)&&Number.isNaN(maxSalary);

    if(Number.isNaN(minSalary)){
      minSalary=0;
    }
    if(Number.isNaN(maxSalary)){
      maxSalary=0;
    }
    if(!bothIsNan){
      if(minSalary >= 0 && maxSalary > 0 && minSalary <= maxSalary){
       /* static logic
       this.tempFilterEmployeesResult = this.employeesFromServer.filter((element) => {
          let currentSalary = parseFloat(element.salary);
          return currentSalary >= minSalary && currentSalary <= maxSalary;
        });
        this.employees=this.tempFilterEmployeesResult;*/
        this.employeeService.fetchEmployeesBySalaryRange(employeeSearchedData).subscribe(
          subscribeEmployees => {
            this.tempFilterEmployeesResult = [];
          
            subscribeEmployees.map(item => {
              var employee = item;
              employee.path=this.image;
              this.tempFilterEmployeesResult.push(employee);
            });
            this.employees=this.tempFilterEmployeesResult;
            this.bindDataToTable();
          }
        ); 
      }
    } 
  }
  
}
