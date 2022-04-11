import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {LibraryModel} from '../../../models/library/library.model';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {

  public libraryItem: LibraryModel;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.libraryItem = data.libraryItem;
  }

  ngOnInit(): void {
  }

}
