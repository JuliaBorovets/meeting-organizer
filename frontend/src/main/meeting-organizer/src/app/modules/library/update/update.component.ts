import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {LibraryService} from '../../../services/library/library.service';
import {ToastrService} from 'ngx-toastr';
import {LibraryModel} from '../../../models/library/library.model';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit, OnDestroy {

  public libraryUpdateForm: FormGroup;
  public submitted = false;
  public isError = false;
  private libraryItem: LibraryModel;
  private readonly subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private libraryService: LibraryService,
              private dialogRef: MatDialogRef<UpdateComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private toastrService: ToastrService) {
    this.libraryItem = data.libraryItem;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createLibraryUpdateForm();

    this.subscription.add(
      this.libraryService.getLibraryById(this.libraryItem.libraryId).subscribe(
        data => {
          this.libraryUpdateForm.setValue(data);
        },
        () => this.isError = true)
    );
  }

  get f(): { [p: string]: AbstractControl } {
    return this.libraryUpdateForm.controls;
  }

  createLibraryUpdateForm(): void {
    this.libraryUpdateForm = this.formBuilder.group({
      libraryId: this.libraryItem.libraryId,
      name: ['', Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      description: ['', Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      isPrivate: [false, null],
      image: null,
      userId: null
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.libraryUpdateForm.invalid) {
      return;
    }

    const libraryUpdateRequest = {
      libraryId: this.f.libraryId.value,
      name: this.f.name.value,
      description: this.f.description.value,
      isPrivate: this.f.isPrivate.value
    };

    this.subscription.add(
      this.libraryService.updateLibrary(libraryUpdateRequest)
        .subscribe(
          () => {
            this.dialogRef.close();
            this.toastrService.success('Success!', 'Updated!');
          },
          () => {
            this.isError = true;
            this.toastrService.error('Error!', 'Update failed!');
          }
        )
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
