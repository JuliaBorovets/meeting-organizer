import {Component, OnInit, ViewChild} from '@angular/core';
import {StorageService} from '../../../services/auth/storage.service';
import {EventComponent} from './event/event.component';
import {LibraryComponent} from './library/library.component';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  links = [
    {
      label: 'Libraries'
    },
    {
      label: 'Events'
    }
  ];
  activeLink = this.links[0];
  userId: number;
  @ViewChild(EventComponent, {static: false})
  private eventComponent: EventComponent;
  @ViewChild(LibraryComponent, {static: false})
  private libraryComponent: LibraryComponent;

  constructor(private storageService: StorageService) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
    this.searchFavorites();
  }

  searchFavorites() {
    if (this.isLibrariesSelected() && this.libraryComponent) {
      this.libraryComponent.findFavorites();
    } else if (this.isEventsSelected() && this.eventComponent) {
      this.eventComponent.findFavorites();
    }
  }

  isLibrariesSelected(): boolean {
    return this.activeLink === this.links[0];
  }

  isEventsSelected(): boolean {
    return this.activeLink === this.links[1];
  }

}
