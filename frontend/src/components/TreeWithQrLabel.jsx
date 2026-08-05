import TreeParkLabel from './TreeParkLabel';

export default function TreeWithQrLabel({
  slug,
  imageUrl,
  alt = '',
  className = '',
  imageClassName = 'h-full w-full object-cover',
  aspectClass = 'aspect-video',
  showLabel = true,
}) {
  return (
    <div className={`overflow-hidden bg-primary/10 ${className}`}>
      <div className={`relative ${aspectClass}`}>
        {imageUrl ? (
          <img src={imageUrl} alt={alt} className={imageClassName} />
        ) : (
          <div className="flex h-full min-h-[12rem] items-center justify-center text-5xl">🌳</div>
        )}
      </div>
      {showLabel && slug && <TreeParkLabel slug={slug} />}
    </div>
  );
}
