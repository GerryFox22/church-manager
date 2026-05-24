import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NewsService } from '../../../core/services/news.service';
import { News } from '../../../shared/models/news.model';

@Component({
  selector: 'app-news-list',
  imports: [RouterLink, DatePipe],
  templateUrl: './news-list.component.html',
  styleUrl: './news-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NewsListComponent {
  private readonly newsService = inject(NewsService);
  protected readonly authService = inject(AuthService);

  protected readonly items = signal<News[]>([]);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);

  constructor() {
    this.loadNews();
  }

  protected deleteItem(id: number): void {
    if (!confirm('Delete this news item?')) {
      return;
    }

    this.newsService.delete(id).subscribe({
      next: () => this.loadNews(),
      error: () => this.errorMessage.set('Unable to delete news item.'),
    });
  }

  private loadNews(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.newsService.getAll().subscribe({
      next: (items) => {
        this.items.set(items);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Unable to load news.');
        this.isLoading.set(false);
      },
    });
  }
}
