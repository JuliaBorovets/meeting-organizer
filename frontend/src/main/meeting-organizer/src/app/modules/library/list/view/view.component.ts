import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {StorageService} from '../../../../services/auth/storage.service';
import {MatDialog} from '@angular/material/dialog';
import {LibraryService} from '../../../../services/library/library.service';
import {LibraryModel} from '../../../../models/library/library.model';
import {InfoComponent} from '../../info/info.component';

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class ViewComponent {

  @Input() libraryItem: LibraryModel;
  userId: number;

  constructor(private libraryService: LibraryService,
              private storageService: StorageService,
              public dialog: MatDialog) {
    // this.userId = this.storageService.getUser.userId;
  }

  openFullInfoView(): void {
    this.dialog.open(InfoComponent, {
      height: 'auto',
      width: '70%',
      data: {library: this.libraryItem}
    });
  }

}
