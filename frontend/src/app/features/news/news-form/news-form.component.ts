import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NewsService } from '../../../core/services/news.service';

@Component({
  selector: 'app-news-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './news-form.component.html',
  styleUrl: './news-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NewsFormComponent {
  private readonly newsService = inject(NewsService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  protected readonly isEditMode = signal(false);
  protected readonly isSubmitting = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  private newsId: number | null = null;

  protected readonly form = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.maxLength(150)]],
    content: ['', Validators.required],
    image: [null as File | null],
  });

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.newsId = Number(idParam);
      this.isEditMode.set(true);
      this.loadExisting(this.newsId);
    }
  }

  private loadExisting(id: number): void {
    this.newsService.getAll().subscribe({
      next: (items) => {
        const item = items.find((news) => news.id === id);
        if (!item) {
          this.errorMessage.set('News item not found.');
          return;
        }

        this.form.patchValue({
          title: item.title,
          content: item.content,
        });
      },
      error: () => this.errorMessage.set('Unable to load news item.'),
    });
  }

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    this.form.patchValue({ image: file });
  }

  protected onSubmit(): void {
    if (this.form.invalid || this.isSubmitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.errorMessage.set(null);

    const payload = this.form.getRawValue();
    const request$ =
      this.isEditMode() && this.newsId !== null
        ? this.newsService.update(this.newsId, payload)
        : this.newsService.create(payload);

    request$.subscribe({
      next: () => {
        void this.router.navigate(['/news']);
      },
      error: () => {
        this.errorMessage.set('Unable to save news item.');
        this.isSubmitting.set(false);
      },
    });
  }
}
