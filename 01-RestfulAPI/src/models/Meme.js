import mongoose from 'mongoose'

const MemeSchema = new mongoose.Schema(
  {
    userId: { type: String, required: true, index: true },
    title: { type: String, required: true, trim: true, maxlength: 140 },
    imageUrl: { type: String, required: true, trim: true },
    tags: { type: [String], default: [] },
    upvotes: { type: Number, default: 0 }
  },
  { timestamps: true }
)

export default mongoose.model('Meme', MemeSchema)
