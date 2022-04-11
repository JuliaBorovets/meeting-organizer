import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {StorageService} from '../../../../services/auth/storage.service';
import {MatDialog} from '@angular/material/dialog';
import {LibraryService} from '../../../../services/library/library.service';
import {LibraryModel} from '../../../../models/library/library.model';
import {InfoComponent} from '../../info/info.component';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {DialogService} from '../../../../services/confirm-dialog.service';
import {UpdateComponent} from '../../update/update.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent implements OnDestroy {

  @Input() libraryItem: LibraryModel;
  @Output() updateLibraryEvent = new EventEmitter();
  userId: number;
  private subscription: Subscription = new Subscription();

  constructor(private libraryService: LibraryService,
              private storageService: StorageService,
              public dialog: MatDialog,
              private toastrService: ToastrService,
              private dialogService: DialogService) {
    this.userId = this.storageService.getUser.userId;
  }

  openFullInfoView(): void {
    this.dialog.open(InfoComponent, {
      height: 'auto',
      width: '100vh',
      data: {libraryItem: this.libraryItem}
    });
  }

  openEditView(): void {
    const editDialogRef = this.dialog.open(UpdateComponent, {
      height: 'auto',
      width: '65vh',
      data: {libraryItem: this.libraryItem}
    });

    this.subscription.add(
      editDialogRef.afterClosed().subscribe(
        () => this.updateLibraryEvent.emit()
      ));
  }

  deleteLibrary(): void {
    this.dialogService.confirmDialog({
      title: 'Confirm deletion',
      message: 'Do you want to confirm this action?',
      confirmCaption: 'Confirm',
      cancelCaption: 'Cancel',
    })
      .subscribe((confirmed) => {
        if (confirmed) {
          this.libraryService.deleteLibrary(this.libraryItem.libraryId).subscribe(
            () => {
              this.toastrService.success('Success!', 'Deleted!');
              this.updateLibraryEvent.emit();
            },
            () => this.toastrService.error('Error!', 'Deletion failed!')
          );
        }
      });
  }

  isLibraryOwner(): boolean {
    return this.libraryItem.userId === this.userId;
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
