import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {StorageService} from '../../../services/auth/storage.service';
import {LibraryService} from '../../../services/library/library.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit, OnDestroy {

  createForm: FormGroup;
  isCreateFailed = false;
  errorMessage = '';
  submitted = false;
  userId: number;
  isError = false;
  isInValid = false;
  private subscription: Subscription;
  private createRequestDialogRef;

  constructor(public dialogRef: MatDialogRef<CreateComponent>,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              public dialog: MatDialog,
              private libraryService: LibraryService) {
    this.userId = storageService.getUser.userId;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createCreateForm();
  }

  createCreateForm(): void {
    this.createForm = this.formBuilder.group({
      name: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      description: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      isPrivate: [false, null]
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
      name: this.f.name.value,
      description: this.f.description.value,
      userId: this.userId,
      isPrivate: this.f.isPrivate.value
    };

    this.subscription = this.libraryService.create(createRequest).subscribe(
      () => {
        this.dialogRef.close();
      },
      () => {
        this.isError = true;
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
