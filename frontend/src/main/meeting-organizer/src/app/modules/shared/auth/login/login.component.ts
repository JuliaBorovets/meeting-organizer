import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../../../services/auth/auth.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {StorageService} from '../../../../services/auth/storage.service';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {RestoreRequestComponent} from '../restore-request/restore-request.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  hide = true;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  submitted = false;
  hidePasswordField = true;
  private subscription: Subscription;
  private restoreRequestDialogRef;

  constructor(public dialogRef: MatDialogRef<LoginComponent>,
              private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });

    if (window.sessionStorage.getItem('user')) {
      this.isLoggedIn = true;
      this.router.navigateByUrl('/organisation').then();
    } else {
      this.isLoggedIn = false;
    }
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }

    const loginResponse = {
      email: this.f.email.value,
      password: this.f.password.value,
    };

    this.subscription = this.authService.login(loginResponse).subscribe(
      user => {
        this.isLoggedIn = true;
        window.sessionStorage.setItem('user', JSON.stringify(user));
        window.sessionStorage.setItem('token', user.token);
        this.storageService.setUser(user);
        this.router.navigateByUrl('/calendar').then();
        this.dialogRef.close();
      },
      err => {
        this.isLoginFailed = true;
      }
    );
  }

  openForgotPasswordDialog(): void {

    this.restoreRequestDialogRef = this.dialog.open(RestoreRequestComponent, {
      height: 'auto',
      width: '65vh'
    }).afterOpened().subscribe(
      () => this.dialogRef.close()
    );
  }

  closeDialog() {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
