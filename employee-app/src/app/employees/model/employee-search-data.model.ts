import { min, of } from "rxjs";

export class EmployeeSearchData {
    public minSalary: string;
    public maxSalary: string;
    public offset: number;
    public limit: number;
    public sort: string;

    constructor( minSalary: string, maxSalary: string,
        offset: number, limit: number, sort: string
        ){
        this.minSalary=minSalary;
        this.maxSalary=maxSalary;
        this.offset=offset;
        this.limit=limit;
        this.sort=sort;
    }
}