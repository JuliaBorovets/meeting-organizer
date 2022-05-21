import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {StorageService} from '../../../services/auth/storage.service';
import {ToastrService} from 'ngx-toastr';
import {EventService} from '../../../services/event/event.service';

@Component({
  selector: 'app-add-access-code',
  templateUrl: './add-access-code.component.html',
  styleUrls: ['./add-access-code.component.scss']
})
export class AddEventAccessCodeComponent implements OnInit, OnDestroy {

  addAccessForm: FormGroup;
  isAddFailed = false;
  errorMessage = '';
  submitted = false;
  userId: number;
  isError = false;
  isInValid = false;
  private subscription: Subscription;

  constructor(public dialogRef: MatDialogRef<AddEventAccessCodeComponent>,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private eventService: EventService,
              private toastrService: ToastrService) {
    this.userId = storageService.getUser.userId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createAddAccessForm();
  }

  createAddAccessForm(): void {
    this.addAccessForm = this.formBuilder.group({
      accessToken: [null, Validators.compose([
        Validators.required])
      ],
    });
  }

  get f() {
    return this.addAccessForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.addAccessForm.invalid) {
      return;
    }

    const addAccessRequest = {
      accessToken: this.f.accessToken.value,
      userId: this.userId,
    };

    this.subscription = this.eventService.addAccessToEventByToken(addAccessRequest).subscribe(
      () => {
        this.toastrService.success('Success!', 'Added!');
        this.dialogRef.close();
      },
      () => {
        this.isError = true;
        this.toastrService.error('Error!', 'Failed to add!');
      }
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
