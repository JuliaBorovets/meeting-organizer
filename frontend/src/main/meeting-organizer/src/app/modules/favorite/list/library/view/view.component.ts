import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {LibraryModel} from '../../../../../models/library/library.model';
import {Subscription} from 'rxjs';
import {LibraryService} from '../../../../../services/library/library.service';
import {ToastrService} from 'ngx-toastr';
import {StorageService} from '../../../../../services/auth/storage.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-view-favorite',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.scss']
})
export class LibraryFavoriteViewComponent implements OnInit, OnDestroy {

  @Input() libraryItem: LibraryModel;
  @Output() updateLibraryEvent = new EventEmitter();
  private subscription: Subscription = new Subscription();
  private userId: number;

  constructor(private libraryService: LibraryService,
              private toastrService: ToastrService,
              private router: Router,
              private storageService: StorageService) {
    this.userId = this.storageService.getUser.userId;
  }

  ngOnInit(): void {
  }

  deleteFromFavorites(): void {
    this.subscription.add(
      this.libraryService.deleteLibraryFromFavorites(this.libraryItem.libraryId, this.userId)
        .subscribe(
          () => {
            this.toastrService.success('Success!', 'Deleted!');
            this.updateLibraryEvent.emit();
          },
          () => this.toastrService.error('Error!', 'Failed to delete!')
        )
    );
  }

  openLibraryContent(): void {
    this.router.navigateByUrl(`library-content/view/${this.libraryItem.libraryId}?libraryFavorite=true`).then(() => {
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
