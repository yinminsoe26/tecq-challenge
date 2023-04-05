import {Injectable}   from '@angular/core';
import { HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Employee } from '../employees/model/employee.model';
import { EmployeeSearchData } from '../employees/model/employee-search-data.model';
import {Observable} from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({providedIn: 'any'})
export class EmployeeService {
    private API_URL:string;
    private ENDPOINT = "/users";
   
    constructor(private http: HttpClient) {
        this.API_URL=`${environment.apiUrl}`;
    }

    saveEmployee(employee: Employee): Observable<Employee> {      
        const headers= new HttpHeaders()
        .set('content-type', 'application/json')
        .set('Access-Control-Allow-Origin', '*');        
         
        return this.http.patch<Employee>(this.API_URL+this.ENDPOINT+"/"+employee.id, employee, { 'headers': headers });
    }
    public fetchEmployeesBySalaryRange(employeeSearchedData:EmployeeSearchData): Observable<Employee[]> {
       // queryParam ?minSalary=0&maxSalary=500000&offset=0&limit=30&sort=+id';
        let queryParams = new HttpParams();
        queryParams = queryParams.append("minSalary", employeeSearchedData.minSalary==''?'0':employeeSearchedData.minSalary);
        queryParams = queryParams.append("maxSalary", employeeSearchedData.maxSalary==''?'0':employeeSearchedData.maxSalary);
        queryParams = queryParams.append("offset", employeeSearchedData.offset);
        queryParams = queryParams.append("limit", employeeSearchedData.limit);
        queryParams = queryParams.append("sort", employeeSearchedData.sort);
        return this.http.get<Employee[]>(this.API_URL+this.ENDPOINT,{params:queryParams});
    }

    deleteEmployee(id: string) {      
        return this.http.delete(this.API_URL+this.ENDPOINT+"/"+id);
    }

    
    uploadCSV(file: File): Observable<HttpEvent<any>> {

        const headers= new HttpHeaders()
        .set('content-type', 'multipart/form-data')
        .set('Access-Control-Allow-Origin', '*');        
         
        const formData: FormData = new FormData();
        formData.append('file', file);
        const req = new HttpRequest('POST', this.API_URL+this.ENDPOINT+"/upload", formData, {
            reportProgress: true,
            responseType: 'text',
            headers: headers
        });

        return this.http.request(req);
  }

 
}