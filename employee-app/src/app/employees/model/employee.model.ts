export class Employee {
    public path: string;
    public id: string;
    public name: string;
    public login: string;
    public salary: string;
    
    constructor(path: string, id: string, name: string, login: string, salary: string){
        this.path = path;
        this.id = id;
        this.name = name;
        this.login = login;
        this.salary = salary;
    }
}