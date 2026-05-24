import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EventsService } from '../../../core/services/events.service';

@Component({
  selector: 'app-events-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './events-form.component.html',
  styleUrl: './events-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EventsFormComponent {
  private readonly eventsService = inject(EventsService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  protected readonly isEditMode = signal(false);
  protected readonly isSubmitting = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  private eventId: number | null = null;

  protected readonly form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    description: ['', Validators.required],
    eventDate: ['', Validators.required],
    location: ['', Validators.required],
    image: [null as File | null],
  });

  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.eventId = Number(idParam);
      this.isEditMode.set(true);
      this.loadExisting(this.eventId);
    }
  }

  private loadExisting(id: number): void {
    this.eventsService.getAll().subscribe({
      next: (items) => {
        const item = items.find((event) => event.id === id);
        if (!item) {
          this.errorMessage.set('Event not found.');
          return;
        }

        this.form.patchValue({
          title: item.title,
          description: item.description,
          eventDate: item.eventDate.slice(0, 16),
          location: item.location,
        });
      },
      error: () => this.errorMessage.set('Unable to load event.'),
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
      this.isEditMode() && this.eventId !== null
        ? this.eventsService.update(this.eventId, payload)
        : this.eventsService.create(payload);

    request$.subscribe({
      next: () => {
        void this.router.navigate(['/events']);
      },
      error: () => {
        this.errorMessage.set('Unable to save event.');
        this.isSubmitting.set(false);
      },
    });
  }
}
