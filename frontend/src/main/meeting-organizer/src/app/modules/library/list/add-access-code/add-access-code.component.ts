import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {StorageService} from '../../../../services/auth/storage.service';
import {LibraryService} from '../../../../services/library/library.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-add-access-code',
  templateUrl: './add-access-code.component.html',
  styleUrls: ['./add-access-code.component.scss']
})
export class AddAccessCodeComponent implements OnInit, OnDestroy {

  createForm: FormGroup;
  isCreateFailed = false;
  errorMessage = '';
  submitted = false;
  userId: number;
  isError = false;
  isInValid = false;
  private subscription: Subscription;

  constructor(public dialogRef: MatDialogRef<AddAccessCodeComponent>,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private libraryService: LibraryService,
              private toastrService: ToastrService) {
    this.userId = storageService.getUser.userId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createCreateForm();
  }

  createCreateForm(): void {
    this.createForm = this.formBuilder.group({
      accessToken: [null, Validators.compose([
        Validators.required])
      ],
    });
  }

  get f() {
    return this.createForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.createForm.invalid) {
      return;
    }

    const createRequest = {
      accessToken: this.f.accessToken.value,
      userId: this.userId,
    };

    this.subscription = this.libraryService.addAccessToLibraryByToken(createRequest).subscribe(
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
