export class LibraryModel {
  libraryId?: number;
  name?: string;
  description?: string;
  image?: Blob;
  isPrivate?: boolean;
  userId?: number;
  isFavorite?: boolean;
  accessToken?: string;
}
