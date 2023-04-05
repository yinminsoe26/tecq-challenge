import { Component, ElementRef, HostListener, Input } from '@angular/core';
import { FormGroup, FormControl, Validators, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { requiredFileType } from 'src/app/shared/requiredFileType';
import { EmployeeService } from '../../../service/employee.service';

@Component({
  selector: 'app-upload-employees',
  templateUrl: './upload-employees.component.html',
  styleUrls: ['./upload-employees.component.css'] 
})
export class UploadEmployeesComponent {
  uploadFile!: File;


  //intialized the employee Service
  constructor(private employeeService: EmployeeService) {}
 
  onUploadToServer(){
    console.log("upload file to server"+this.uploadFile);
    this.employeeService.uploadCSV(this.uploadFile);
  }
}
