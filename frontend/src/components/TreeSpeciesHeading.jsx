/**
 * Scientific name is the primary label; common name is secondary.
 */
export function TreeSpeciesHeading({
  scientificName,
  commonName,
  family,
  variant = 'inline',
  inverted = false,
}) {
  if (!scientificName && !commonName) return null;

  if (variant === 'hero') {
    return (
      <div>
        <h1
          className={`font-serif text-3xl font-semibold italic leading-tight sm:text-4xl lg:text-5xl ${
            inverted ? 'text-white' : 'text-primary-dark'
          }`}
        >
          {scientificName}
        </h1>
        {commonName && (
          <p className={`mt-2 text-sm font-medium tracking-wide sm:text-base ${inverted ? 'text-white/75' : 'text-gray-500'}`}>
            {commonName}
          </p>
        )}
        {family && (
          <p className={`mt-1 text-xs sm:text-sm ${inverted ? 'text-white/60' : 'text-gray-400'}`}>
            {family}
          </p>
        )}
      </div>
    );
  }

  if (variant === 'directory') {
    return (
      <span className="min-w-0">
        <span className="block truncate font-serif text-base font-semibold italic text-primary-dark group-hover:text-primary">
          {scientificName}
        </span>
        {commonName && (
          <span className="mt-0.5 block truncate text-sm text-gray-500">
            {commonName}
          </span>
        )}
      </span>
    );
  }

  return (
    <div>
      <h1 className="font-serif text-2xl font-semibold italic text-primary-dark sm:text-3xl">
        {scientificName}
      </h1>
      {commonName && (
        <p className="mt-1 text-sm font-medium text-gray-500 sm:text-base">{commonName}</p>
      )}
      {family && <p className="mt-1 text-sm text-gray-400">{family}</p>}
    </div>
  );
}
