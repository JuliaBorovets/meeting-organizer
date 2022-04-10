import {Component, OnDestroy, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import {CreateComponent} from '../create/create.component';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-create-new-password',
  templateUrl: './create-new-password.component.html',
  styleUrls: ['./create-new-password.component.scss']
})
export class CreateNewPasswordComponent implements OnInit, OnDestroy {

  private createDialogRef;
  private subscription: Subscription = new Subscription();

  constructor(private toastrService: ToastrService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.openCreateDialog();
  }

  public showSuccess(): void {
    this.toastrService.success('Success!', 'Registration is verified!');
  }

  public showError(): void {
    this.toastrService.error('Error!', 'Registration is not verified!');
  }

  openCreateDialog(): void {

    this.createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '65vh'
    });

    this.subscription.add(
      this.createDialogRef.afterClosed().subscribe(
        () => {
        },
        () => {
        })
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
