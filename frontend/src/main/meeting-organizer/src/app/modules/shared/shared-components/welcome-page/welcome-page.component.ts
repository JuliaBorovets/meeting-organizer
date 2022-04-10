import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../../auth/login/login.component';
import {Subscription} from 'rxjs';
import {RegisterComponent} from '../../auth/register/register.component';

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.scss']
})
export class WelcomePageComponent implements OnInit, OnDestroy {

  public isError = false;
  private subscription: Subscription = new Subscription();
  private loginDialogRef;

  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  openLoginDialog(): void {

    this.loginDialogRef = this.dialog.open(LoginComponent, {
      height: 'auto',
      width: '65vh'
    });

    this.subscription.add(
      this.loginDialogRef.afterClosed().subscribe(
        () => {
        },
        () => {
          this.isError = true;
        })
    );
  }

  openRegisterDialog(): void {

    const registerDialogRef = this.dialog.open(RegisterComponent, {
      height: 'auto',
      width: '70%'
    });

    this.subscription.add(
      registerDialogRef.afterClosed().subscribe(
        () => {
        },
        () => {
          this.isError = true;
        })
    );
  }


  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
