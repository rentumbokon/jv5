import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  registering: boolean = false;
  isLoggedIn: boolean = false;
  loggingIn: boolean = false;
  constructor() { }

  setRegistering(r: boolean) {
    this.registering = r;
  }

  GetRegistering(){
    return this.registering;
  }

  setLoggedIn(l: boolean) {
    this.isLoggedIn = l;
  }

  GetLoggedIn() {
    return this.isLoggedIn;
  }

  setLoggingIn(l: boolean) {
    this.loggingIn = l;
  }

  getLoggingIn() {
    return this.loggingIn;
  }
}
