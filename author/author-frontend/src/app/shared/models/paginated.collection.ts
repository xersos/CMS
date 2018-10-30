export class PaginatedCollection {
  items: Array<any>;
  totalItems: number;
  totalPages: number;
  currentPage: number;
  last: boolean;
  first: boolean;
  pages: Array<number> = [];
}
